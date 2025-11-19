using HackHeroes.API.Models;
using Microsoft.Extensions.Primitives;

namespace HackHeroes.API;

abstract class HackHeroesAPI
{
	public static WebApplication App { get; private set; }
	public static void Main(string[] args)
	{
		string rootApiToken = CreateRootApiToken();
		var builder = WebApplication.CreateBuilder(args);

		builder.Services.AddEndpointsApiExplorer();
		builder.Services.AddSwaggerGen();

		App = builder.Build();

		if (App.Environment.IsDevelopment())
		{
			App.UseSwagger();
			App.UseSwaggerUI();
		}

		App.UseHttpsRedirection();

		App.MapGet("/status", () => new Status())
			.WithName("GetStatus")
			.WithDescription("Returns server status")
			.WithOpenApi();

		App.MapPost("/translate", (HttpContext ctx, TranslationRequest? translationRequest) =>
			{
				string? token = ctx.Request.Headers.Authorization.FirstOrDefault()?.Split(" ").Last();
				if (token is not { } token2 || !token2.Equals(rootApiToken))
					return Results.Unauthorized();

				return translationRequest is null ? Results.BadRequest() : Results.Ok(new TranslationResponse("translated message"));
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