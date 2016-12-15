import {Component, OnInit} from '@angular/core';
import {AuthService} from "./auth/service/AuthService";
import {jCredential} from "./model/Credential";
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
<p>{{aclAdmin}}</p>
<p>{{aclData}}</p>
<p>{{aclOwner}}</p>
<p>jToken == {{token}}</p>`,
})
export class AppComponent implements OnInit {
  authType: string[];
  token: any;
  typeSelected: string = 'BASIC';
  private aclAdmin: string[];
  private aclRole: string[];
  private aclData: string[];
  private aclOwner: string[];


//
  constructor(private authService: AuthService, private aclService: AclService) {
  }

  ngOnInit() {
    this.authService.getAuthTypeEnum().subscribe(
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
    AuthService.log();

    this.aclService.getEnumRole().subscribe(roles => this.aclRole = roles )
    this.aclService.getEnumAdmin().subscribe(admin => this.aclAdmin = admin )
    this.aclService.getEnumData().subscribe(data => this.aclData = data )
    this.aclService.getEnumOwner().subscribe(owner => this.aclOwner = owner )
  }



}
