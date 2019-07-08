import {environment} from "../../../environments/environment";

export class PageableComponent {

  page: number = 0;
  pageSize: number = environment.api.defaultPageSize;

  changePage(value) {
    this.page = value;
  }

  changePageSize(value) {
    this.pageSize = value;
  }
}
