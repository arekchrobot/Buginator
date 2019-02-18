import {Component, Input, OnDestroy, OnInit, Renderer2, ViewEncapsulation} from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class LoginComponent implements OnInit, OnDestroy {

  @Input() username: string | null;
  @Input() password: string | null;

  activeTab = 'login';

  constructor(private renderer: Renderer2) { }

  ngOnInit() {
    this.renderer.addClass(document.body, 'login');
  }

  ngOnDestroy(): void {
    this.renderer.removeClass(document.body, 'login');
  }

  changeTab(tab) {
    console.log('dupa');
    this.activeTab = tab;
  }
}
