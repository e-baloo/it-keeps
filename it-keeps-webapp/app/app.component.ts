import {Component, OnInit} from '@angular/core';
import {AuthService} from "./service/AuthService";

@Component({
  selector: 'my-app',
  template: `<h1>Hello {{name}}</h1><p>{{enum | json}}</p>`,
})
export class AppComponent implements OnInit {
  name = 'Angular';
  enum: any = {};

  constructor(private authService: AuthService) {
  }

  ngOnInit() {
    this.authService.getEnumAuthType().subscribe(
      data => this.enum = data
    );


  }


}
