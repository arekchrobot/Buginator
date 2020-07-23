export class ApplicationError {
  id: number;
  title: string;
  description: string;
  status: string;
  severity: string;
  lastOccurrence: Date;
  count: number;
}

export class ApplicationErrorDetails {
  id: number;
  title: String;
  description: String;
  status: ErrorStatus;
  severity: ErrorSeverity;
  dateTime: Date;
  lastOccurrence: Date;
  count: number;
  requestUrl: String;
  requestMethod: String;
  requestParams: String;
  queryParams: String;
  requestHeaders: String;
  stackTrace: String;
  userAgent: UserAgent;
}

export enum ErrorStatus {
  CREATED = 'CREATED', REOPENED = 'REOPENED', ONGOING = 'ONGOING', RESOLVED = 'RESOLVED'
}

export enum ErrorSeverity {
  WARNING = 'WARNING', ERROR = 'ERROR', CRITICAL = 'CRITICAL'
}

export class UserAgent {
  manufacturer: String;
  device: String;
  operatingSystem: String;
  browser: String;
  country: String;
  language: String;
}
