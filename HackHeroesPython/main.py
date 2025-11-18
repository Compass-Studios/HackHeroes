import typer # type: ignore
from typing import List
from transformers import AutoModelForCausalLM, AutoTokenizer
from rich.console import Console
from rich.panel import Panel
import warnings

model_name = "Qwen/Qwen2.5-3B-Instruct"
warnings.filterwarnings("ignore")

app = typer.Typer(help="CLI AI Translator")
console = Console()

def load_model():
    model = AutoModelForCausalLM.from_pretrained(
        model_name,
        dtype="auto",
        device_map="auto"
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
        temperature=0.4,
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
        system_prompt = """Jesteś nastolatkiem z Generacji Z. Przetłumacz podany sztywny tekst na luźny slang.
            Przykłady:
            Tekst: "To jest bardzo zabawne." -> Slang: "Totalna beka z tego."
            Tekst: "Czuję się zażenowany." -> Slang: "Ale cringe."
            Tekst: "Zgadzam się z toba." -> Slang: "Rel totalnie."
        """        
        title = "Formalny -> Slang"
        style = "bold magenta"
    else:
        system_prompt = """Jesteś ekspertem językowym. Twoim zadaniem jest przetłumaczenie slangu młodzieżowego na poprawną polszczyznę zrozumiałym dla seniora.
            W nawiasie dodaj krótkie wyjaśnienie kluczowego słowa.

            Przykłady:
            Slang: "Ta impreza to był totalny sztos." 
            Tłumaczenie: "To przyjęcie było rewelacyjne (sztos oznacza coś świetnego, wyjątkowego)."

            Slang: "Ten film jest cringe."
            Tłumaczenie: "Ten film wywołuje uczucie zażenowania (cringe to wstyd za kogoś/coś, lub zażenowanie)."

            Slang: "Idę na trening, muszę grindować."
            Tłumaczenie: "Idę na trening, muszę ciężko pracować (grindować to żmudnie dążyć do celu)."
        """
        title = "Slang -> Formalny"
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