import typer # type: ignore
from typing import List
from transformers import AutoModelForCausalLM, AutoTokenizer
from rich.console import Console
from rich.panel import Panel
import warnings
import logging
import torch
import transformers

model_name = "Qwen/Qwen2.5-3B-Instruct"
warnings.filterwarnings("ignore")
logging.getLogger("transformers").setLevel(logging.ERROR)
transformers.logging.set_verbosity_error()

app = typer.Typer(help="CLI AI Translator")
console = Console()

def load_model():

    model = AutoModelForCausalLM.from_pretrained(
        model_name,
        device_map="auto",
        dtype="auto"
    )
    tokenizer = AutoTokenizer.from_pretrained(model_name)
    return model, tokenizer


def generate_response(model, tokenizer, system_prompt, user_text):
    messages = [
        {"role": "system", "content": system_prompt},
        {"role": "user", "content": user_text}
    ]
    text = tokenizer.apply_chat_template(
        messages, tokenize=False, add_generation_prompt=True)
    model_inputs = tokenizer([text], return_tensors="pt").to(model.device)

    generated_ids = model.generate(
        model_inputs.input_ids,
        max_new_tokens=256,
        do_sample=True,
        temperature=0.7,
        top_p=0.9
    )

    generated_ids = [output_ids[len(input_ids):] for input_ids, output_ids in zip(
        model_inputs.input_ids, generated_ids)]
    response = tokenizer.batch_decode(
        generated_ids, skip_special_tokens=True)[0]
    return response

@app.command()
def translate(text: List[str] = typer.Argument(..., help="Text for translation"), 
              direction: str = typer.Option("senior", "--do", "-d", help="Direction for translation")
              ):
    user_text = " ".join(text)
    if direction == "slang":
        system_prompt = """Jesteś młodą osobą z Generacji Z, która używa naturalnego, nowoczesnego slangu. 
            Twoim zadaniem jest przetłumaczenie sztywnego, oficjalnego tekstu na luźny język potoczny, używany przez młodych ludzi w internecie i na co dzień.
            Nie używaj przestarzałych zwrotów (jak "klawo", "czad"). Używaj słów takich jak: rel, cringe, baza, slay, odklejka, dymy,essa, mrozi, sigma.
            Zachowaj sens wypowiedzi, ale zmień formę na maksymalnie luźną.

            Przykłady:
            Tekst: "To jest bardzo zabawne." -> Slang: "Totalna beka, nie wytrzymam."
            Tekst: "Czuję się zażenowany tą sytuacją." -> Slang: "Jaki cringe, mrozi mnie totalnie."
            Tekst: "Zgadzam się z tobą w stu procentach." -> Slang: "Rel, baza totalna."
            Tekst: "On zachowuje się dziwnie." -> Slang: "Ale odklejka, typ ma swój świat."
            Tekst: "Wyglądasz świetnie." -> Slang: "Slay, wyglądasz jak sigma."
        """
        title = "Formalny -> Slang (Gen Z)"
        style = "bold magenta"
    else:
        system_prompt = """Jesteś ekspertem językowym i tłumaczem międzypokoleniowym. 
            Twoim zadaniem jest przetłumaczenie młodzieżowego slangu na poprawną, kulturalną polszczyznę, zrozumiałą dla osoby starszej (seniora).
            Kluczowe jest, abyś nie tylko przetłumaczył zdanie, ale w nawiasie wyjaśnił znaczenie trudnych słów slangowych w prosty sposób.

            Przykłady:
            Slang: "Ta impreza to był totalny sztos, ale potem były dymy."
            Tłumaczenie: "To przyjęcie było rewelacyjne, ale później wybuchła awantura. (sztos - coś świetnego; dymy - kłótnia, zamieszanie)"

            Slang: "Ten film to cringe, totalna odklejka."
            Tłumaczenie: "Ten film wywołuje uczucie zażenowania i jest zupełnie bez sensu. (cringe - wstyd, zażenowanie; odklejka - oderwanie od rzeczywistości)"

            Slang: "Idę na trening, muszę grindować żeby być sigmą."
            Tłumaczenie: "Idę na trening, muszę ciężko pracować, żeby stać się niezależnym i silnym człowiekiem. (grindować - ciężko pracować; sigma - osoba pewna siebie, niezależna)"
        """
        title = "Slang -> Formalny (Senior)"
        style = "bold cyan"
    console.print("Loading model...")
    model, tokenizer = load_model()
    
    console.print("Generating")
    result = generate_response(model, tokenizer, system_prompt, user_text)
    
    console.print(f"\n[dim]Original: {user_text}[/dim]")
    console.print(Panel(result, title=title,
                  style=style, border_style="white"))

if __name__ == "__main__":
    app()