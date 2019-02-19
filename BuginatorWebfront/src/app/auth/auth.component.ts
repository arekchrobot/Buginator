import {Component, OnDestroy, OnInit, Renderer2, ViewEncapsulation} from '@angular/core';

@Component({
  selector: 'buginator-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class AuthComponent implements OnInit, OnDestroy {

  activeTab: string = 'login';

  constructor(private renderer: Renderer2) { }

  ngOnInit() {
    this.renderer.addClass(document.body, 'login');
  }

  ngOnDestroy() {
    this.renderer.removeClass(document.body, 'login');
  }

  changeTab(tab) {
    this.activeTab = tab;
  }
}
