# HackHeroes
Project for HackHeroes 2025. The project is a translation app, that helps people from different generations to communicate with each other more easily. The app uses AI to translate slang and idioms from one generation to another.

## Development
### Prerequisites
- .NET SDK 9.0

### API
Restore NuGet packages
```shell
dotnet restore --project HackHeroesAPI/HackHeroes.API/HackHeroes.API.csproj
```

Run the API
```shell
dotnet run --project HackHeroesAPI/HackHeroes.API/HackHeroes.API.csproj
```
The API will be available at `http://localhost:5014`. If you need HTTPS run
```shell
 dotnet run --project HackHeroesAPI/HackHeroes.API/HackHeroes.API.csproj --launch-profile https
```
The API will be available at `http://localhost:7026`. You will also have to trust the development certificate if you haven't done so already
```shell
dotnet dev-certs https --trust
```
The API will generate `root.txt` in HackHeroes.API folder, which contains the Bearer token for accessing the service.

### Python

To run the python script do this: (for linux)

```shell
python -m venv venv
source venv/bin/activate 
```
Then install the dependencies
```shell
pip install requirements.txt -r
```
Sample command to the cli looks like this:
```shell
python main.py translate "Text" -d [OPTION]
```

## Options

slang - It translates from formal speech to slang <br>
senior/no flag as it is default one - translates from slang to formal speech
