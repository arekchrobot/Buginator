export class BaseApplication {
  id: number;
  name: string;
}

export class UserApplication extends BaseApplication {
  modify: boolean;
}

export class Application extends UserApplication {
  allErrorCount: number;
  lastWeekErrorCount: number;
}
