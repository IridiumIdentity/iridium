import { PageInfo } from './page-info';

export class PagedListResponse<T> {

  pageInfo!: PageInfo;

  data: T[] = [];
}
