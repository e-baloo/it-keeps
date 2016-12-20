import {BrowserModule}  from '@angular/platform-browser';
import {HttpModule, BaseRequestOptions}     from '@angular/http';
import {FormsModule}    from '@angular/forms';
import {AppComponent}   from './app.component';
import {AuthService}    from "./service/auth.service";
import {AUTH_PROVIDERS, provideAuth } from 'angular2-jwt';
import {AclService}     from "./service/acl.service";
import {HomeComponent} from "./home/home.component";
import {LoginComponent} from "./login/login.component";
import {AlertComponent} from "./directives/alert.component";
import {AuthGuard} from "./login/auth.guard";
import {routing} from "./app.routing";
import {NgModule} from "@angular/core";
import {AlertService} from "./service/alert.service";

@NgModule({
  imports: [
      BrowserModule,
      HttpModule,
      FormsModule,
      routing
  ],
  declarations: [
    AppComponent,
    AlertComponent,
    HomeComponent,
    LoginComponent,
  ],
  providers: [
    AuthService,
    AclService,
    AlertService,
    AUTH_PROVIDERS,
    AuthGuard,
    BaseRequestOptions,
  
  
    provideAuth({
      headerName: 'x-access-token',
      tokenName: 'token',
      tokenGetter: (() => AuthService.getInstance().getToken()),
      globalHeaders: [{'Content-Type':'application/json'}],
      noJwtError: true,
      noTokenScheme: true})
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
