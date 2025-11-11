namespace HackHeroes.API.Models;

public class Status
{
	public string EnvironmentName { get; set; } = HackHeroesAPI.App.Environment.EnvironmentName;
	public string WebRootPath { get; set; } = HackHeroesAPI.App.Environment.WebRootPath;
	public string ApplicationName { get; set; } = HackHeroesAPI.App.Environment.ApplicationName;
}