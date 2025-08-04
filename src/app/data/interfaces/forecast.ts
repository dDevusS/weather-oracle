export interface Forecast {
  locationId: number,
  locationName: string,
  countryCode: string,
  state: string,
  iconUrl: string,
  description: string,
  temperature: number,
  pressure: number,
  humidity: number
}
