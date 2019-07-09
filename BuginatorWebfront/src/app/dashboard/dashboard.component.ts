import {Component, OnDestroy, OnInit, Renderer2} from '@angular/core';
import {SessionService} from "../shared/service/session.service";

@Component({
  selector: 'buginator-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit, OnDestroy {

  collapsed: boolean = true;

  constructor(private renderer: Renderer2, private sessionService: SessionService) { }

  ngOnInit() {
    this.renderer.addClass(document.body, 'dashboard');
  }

  ngOnDestroy(): void {
    this.renderer.removeClass(document.body, 'dashboard');
  }

  setCollapse() {
    this.collapsed = !this.collapsed;
  }

  get canViewApplications() {
    return this.sessionService.hasPermission("read_application");
  }

}
