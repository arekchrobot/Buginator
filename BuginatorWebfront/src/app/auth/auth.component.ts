import {Component, OnDestroy, OnInit, Renderer2, ViewEncapsulation} from '@angular/core';
import {AuthService} from "./auth.service";

@Component({
  selector: 'buginator-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class AuthComponent implements OnInit, OnDestroy {

  activeTab: string = 'login';
  private registerSuccess: boolean = false;

  constructor(private renderer: Renderer2, private authService: AuthService) { }

  ngOnInit() {
    this.renderer.addClass(document.body, 'login');
    this.authService.registerSuccessSubject.subscribe((success: boolean) => {
      if(success) {
        this.changeTab('login');
        this.registerSuccess = true;
      }
    });
  }

  ngOnDestroy() {
    this.renderer.removeClass(document.body, 'login');
  }

  isRegisterSuccess() {
    return this.registerSuccess;
  }

  changeTab(tab) {
    this.activeTab = tab;
    this.registerSuccess = false;
  }
}
