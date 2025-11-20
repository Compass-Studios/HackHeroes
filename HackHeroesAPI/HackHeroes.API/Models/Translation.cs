namespace HackHeroes.API.Models;

public class TranslationRequest
{
	public string Message { get; set; }
	public string Direction { get; set; } // "genz-to-genx" or "genx-to-genz"
}
public class TranslationResponse(string message)
{
	public string Message { get; init; } = message;
}