export interface ApiDataResponse<Body> {

  code: string;

  message: string;

  successful: boolean;

  data: Body;

}
