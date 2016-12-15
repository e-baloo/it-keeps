import {Component, OnInit} from '@angular/core';
import {AuthService} from "./auth/service/AuthService";
import {jCredential} from "./model/Credential";
import {JwtHelper} from "angular2-jwt";
import {AclService} from "./service/AclService";

@Component({
  selector: 'my-app',
  template: `
<h1>IT-Keeps</h1>

<select [(ngModel)]="typeSelected" (change)="onChange($event)">
  <option *ngFor="let type of authType" [value]="type" >
    {{type}}
  </option>
</select>

<p>{{aclRole}}</p>
<p>jToken == {{token}}</p>`,
})
export class AppComponent implements OnInit {
  authType: string[];
  aclRole: string[];
  token: any;
  typeSelected: string = 'BASIC';

//
  constructor(private authService: AuthService, private aclService: AclService) {
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

    this.useJwtHelper();

    this.aclService.getEnumAclRole().subscribe(roles => this.aclRole = roles )
  }



  useJwtHelper() {
    let jwtHelper: JwtHelper = new JwtHelper();

    let token = localStorage.getItem('token');

    console.log(
      token,
      jwtHelper.decodeToken(token),
      jwtHelper.getTokenExpirationDate(token),
      jwtHelper.isTokenExpired(token)
    );
  }

}
