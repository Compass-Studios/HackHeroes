using Google.GenAI;
using Google.GenAI.Types;

namespace HackHeroes.API.Services.TranslationService;

public class GeminiTranslationService(string? apiKey) : ITranslationService
{
	private const string _systemInstruction =
		"Jesteś silnikiem tłumacza który tłumaczy między slangiem Gen Z a Gen X, oraz na odwrót. "
		+ "Jeśli użytkownik w kierunku zapisze: genx-to-genz, tłumacz wiadomość ze slangu Generacji X do slangu Generacji Z. "
		+ "Jeśli użytkownik w kierunku zapisze: genz-to-genx, tłumacz wiadomość ze slangu Generacji Z do slangu Generacji X. "
		+ "Upewnij się, że tłumaczenia są dokładne i zachowują oryginalne znaczenie wiadomości."
		+ "Napisz tylko przetłumaczoną wiadomość bez dodatkowych komentarzy. Przenieś definicje na nową linię na końcu odpowiedzi."
		+ "Przykłady:\n"
		+ "Slang: \"Ta impreza to był totalny sztos, ale potem były dymy.\"\n"
		+ "Tłumaczenie: \"To przyjęcie było rewelacyjne, ale później wybuchła awantura. (sztos - coś świetnego; dymy - kłótnia, zamieszanie)\"\n\n"
		
		+ "Slang: \"Ten film to cringe, totalna odklejka.\"\n"
		+ "Tłumaczenie: \"Ten film wywołuje uczucie zażenowania i jest zupełnie bez sensu. (cringe - wstyd, zażenowanie; odklejka - oderwanie od rzeczywistości)\"\n"
		
		+ "\nSlang: \"Idę na trening, muszę grindować żeby być sigmą.\""
		+ "\nTłumaczenie: \"Idę na trening, muszę ciężko pracować, żeby stać się niezależnym i silnym człowiekiem. (grindować - ciężko pracować; sigma - osoba pewna siebie, niezależna)\"";

	private readonly Client _client = new(apiKey: apiKey);
	
	public async Task<string?> Translate(string message, string direction)
	{
		if (apiKey is null)
			return null;
		
		GenerateContentResponse? res = await _client.Models.GenerateContentAsync("gemini-2.5-flash", $"Instrukcje systemowe: {_systemInstruction}\nKierunek: {direction}\nWiadomość: {message}");

		string? response = res.Candidates?[0].Content?.Parts?[0].Text;
		return response;
	}
}