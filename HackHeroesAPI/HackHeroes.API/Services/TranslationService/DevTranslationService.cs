namespace HackHeroes.API.Services.TranslationService;

public class DevTranslationService : ITranslationService
{
	public async Task<string?> Translate(string message, string direction)
	{
		return "Translated message";
	}
}