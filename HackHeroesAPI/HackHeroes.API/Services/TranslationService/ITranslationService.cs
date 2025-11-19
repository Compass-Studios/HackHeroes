namespace HackHeroes.API.Services.TranslationService;

public interface ITranslationService
{
	public Task<string?> Translate(string message, string direction);
}