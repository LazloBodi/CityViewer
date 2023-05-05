export class City {
  id!: number;
  name!: string;
  photo!: string;
}

export class CityPageResponse {
    cities!: City[];
    page!: number;
    size!: number;
    totalPages!: number;
}
