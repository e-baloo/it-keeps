import {NgModule}       from '@angular/core';
import {BrowserModule}  from '@angular/platform-browser';
import {HttpModule}     from '@angular/http';
import {FormsModule}    from '@angular/forms';
import {AppComponent}   from './app.component';
import {AuthService}    from "./auth/service/auth..service";
import {AUTH_PROVIDERS, provideAuth } from 'angular2-jwt';
import {AclService}     from "./service/AclService";

@NgModule({
  imports: [BrowserModule, HttpModule, FormsModule],
  declarations: [AppComponent],
  bootstrap: [AppComponent],
  providers: [AuthService, AclService, AUTH_PROVIDERS,

    provideAuth({
      headerName: 'x-access-token',
      tokenName: 'token',
      tokenGetter: (() => AuthService.getInstance().getToken()),
      globalHeaders: [{'Content-Type':'application/json'}],
      noJwtError: true,
      noTokenScheme: true})
  ]
})
export class AppModule {
}
