import {Component, OnInit} from '@angular/core';
import {AuthService} from "./auth/service/AuthService";
import {jCredential} from "./model/Credential";
import {jToken} from "./model/Token";

@Component({
  selector: 'my-app',
  template: `
<h1>IT-Keeps</h1>

<select [(ngModel)]="typeSelected" (change)="onChange($event)">
  <option *ngFor="let type of authType" [value]="type" >
    {{type}}
  </option>
</select>

<p>jToken == {{token?.getToken()}}</p>`,
})
export class AppComponent implements OnInit {
  authType: string[];
  token: jToken;
  typeSelected: string = 'BASIC';

//
  constructor(private authService: AuthService) {
  }

  ngOnInit() {
    this.authService.getEnumAuthType().subscribe(
      data => this.authType = data
    );


    let cred: jCredential = new jCredential;
    cred.getLight().setName("marc");
    cred.setPassword("marc");

    console.log(JSON.stringify(cred));

    this.authService.login(cred).subscribe(
      token => this.token = token,
      error => console.error(error),
      () => console.log("end")
    );

  }

  onChange(value:any) {
    console.log(this.typeSelected);
  }


}
