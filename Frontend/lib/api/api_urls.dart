class ApiUrls
{
  static const String baseURL = String.fromEnvironment(
    'API_BASE_URL',
    defaultValue: 'https://expansetracker-ipyr.onrender.com',
  );
}