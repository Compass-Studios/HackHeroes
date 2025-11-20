using HackHeroes.API.Models;
using HackHeroes.API.Services.TranslationService;

namespace HackHeroes.API;

abstract class HackHeroesAPI
{
	public static WebApplication App { get; private set; }
	private static ITranslationService _translationService;
	public static void Main(string[] args)
	{
		string rootApiToken = CreateRootApiToken();
		var builder = WebApplication.CreateBuilder(args);

		builder.Services.AddEndpointsApiExplorer();
		builder.Services.AddSwaggerGen();

		App = builder.Build();
		_translationService = new DevTranslationService();

		if (App.Environment.IsDevelopment())
		{
			App.UseSwagger();
			App.UseSwaggerUI();
		}

		App.UseHttpsRedirection();

		App.MapGet("/status", async (HttpContext ctx) =>
			{
				bool isValidApiKey = false;
				string? token = ctx.Request.Headers.Authorization.FirstOrDefault()?.Split(" ").Last();
				if (token != null && token.Equals(rootApiToken))
					isValidApiKey = true;
				
				return new Status(isValidApiKey);
			})
			.WithName("GetStatus")
			.WithDescription("Returns server status")
			.WithOpenApi();

		App.MapPost("/translate", async (HttpContext ctx, TranslationRequest? translationRequest) =>
			{
				string? token = ctx.Request.Headers.Authorization.FirstOrDefault()?.Split(" ").Last();
				if (token is not { } token2 || !token2.Equals(rootApiToken))
					return Results.Unauthorized();

				if (translationRequest is null)
					return Results.BadRequest();

				string? translatedMessage = await _translationService.Translate(translationRequest.Message, translationRequest.Direction);

				if (translatedMessage is null)
					return Results.StatusCode(500);

				return Results.Ok(new TranslationResponse(translatedMessage));
			})
			.WithName("PostTranslate")
			.WithDescription("Translates given message from/to Gen Z slang. Requires Bearer authentication token to be set")
			.WithOpenApi();

		App.Run();
	}

	private static string CreateRootApiToken()
	{
		string path = Path.Combine(Directory.GetCurrentDirectory(), "root.txt");
		if (File.Exists(path))
			return File.ReadAllText(path);

		string token = TokenGenerator.Generate();
		File.WriteAllText(path, token);
		return token;
	}
}