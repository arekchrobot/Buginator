import {SessionService} from "./session.service";
import {environment} from "../../../environments/environment";

describe('SessionService', () => {

  let sessionService: SessionService;

  beforeEach(() => {
    sessionStorage.removeItem(environment.api.loggedUserStorage);
    sessionService = new SessionService();
  });

  it('should mark user as not authenticated', () => {
    expect(sessionService.isAuthenticated()).toBeFalsy();
  });

  it('should correctly fetch user and mark it as authenticated', () => {
    //given
    sessionStorage.setItem(environment.api.loggedUserStorage,
      '{"name":"test@gmail.com","email":"test@gmail.com","permissions":[]}');

    //then
    expect(sessionService.isAuthenticated()).toBeTruthy();
  });

  it('should confirm that user has permission', () => {
    //given
    sessionStorage.setItem(environment.api.loggedUserStorage,
      '{"name":"test@gmail.com","email":"test@gmail.com","permissions":["test"]}');

    //then
    expect(sessionService.hasPermission("test")).toBeTruthy();
  });

  it('should not find permission for user', () => {
    //given
    sessionStorage.setItem(environment.api.loggedUserStorage,
      '{"name":"test@gmail.com","email":"test@gmail.com","permissions":["test"]}');

    //then
    expect(sessionService.hasPermission("notExisitng")).toBeFalsy();
  });

  it('should reject permission if user is not defined', () => {
    //then
    expect(sessionService.hasPermission("test")).toBeFalsy();
  });

  it('should logout user', () => {
    //given
    sessionStorage.setItem(environment.api.loggedUserStorage,
      '{"name":"test@gmail.com","email":"test@gmail.com","permissions":[]}');
    expect(sessionService.isAuthenticated()).toBeTruthy();

    //when
    sessionService.logout();

    //then
    expect(sessionService.isAuthenticated()).toBeFalsy();
  });
});
