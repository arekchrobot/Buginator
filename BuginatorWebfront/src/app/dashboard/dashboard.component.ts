import {Component, OnDestroy, OnInit, Renderer2} from '@angular/core';
import {SessionService} from "../shared/service/session.service";
import {CookieService} from "ngx-cookie-service";
import {environment} from "../../environments/environment";
import {Permissions} from "../auth/model/permissions.model";

@Component({
  selector: 'buginator-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit, OnDestroy {

  collapsed: boolean = true;

  constructor(private renderer: Renderer2, private sessionService: SessionService,
              private cookieService: CookieService) { }

  ngOnInit() {
    this.renderer.addClass(document.body, 'dashboard');
  }

  ngOnDestroy(): void {
    this.renderer.removeClass(document.body, 'dashboard');
  }

  setCollapse() {
    this.collapsed = !this.collapsed;
  }

  get loggedUsername() {
    return this.sessionService.username;
  }

  logout() {
    this.cookieService.delete(environment.api.accessTokenCookie);
    this.sessionService.logout();
  }

  get canViewApplications() {
    return this.sessionService.hasPermission(Permissions.READ_APPLICATION);
  }

}
