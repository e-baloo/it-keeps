import {NgModule}       from '@angular/core';
import {BrowserModule}  from '@angular/platform-browser';
import {HttpModule}     from '@angular/http';
import {FormsModule}    from '@angular/forms';
import {AppComponent}   from './app.component';
import {AuthService}    from "./auth/service/AuthService";
import {AUTH_PROVIDERS} from 'angular2-jwt';
import {AclService}     from "./service/AclService";

@NgModule({
  imports: [BrowserModule, HttpModule, FormsModule],
  declarations: [AppComponent],
  bootstrap: [AppComponent],
  providers: [AuthService, AclService, AUTH_PROVIDERS]
})
export class AppModule {
}
