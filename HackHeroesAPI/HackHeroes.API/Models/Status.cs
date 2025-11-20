namespace HackHeroes.API.Models;

public class Status(bool isValidApiKey)
{
	public string EnvironmentName { get; set; } = HackHeroesAPI.App.Environment.EnvironmentName;
	public string WebRootPath { get; set; } = HackHeroesAPI.App.Environment.WebRootPath;
	public string ApplicationName { get; set; } = HackHeroesAPI.App.Environment.ApplicationName;
	public bool IsValidApiKey { get; set; } = isValidApiKey;
}