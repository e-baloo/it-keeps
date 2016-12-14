import {NgModule}      from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HttpModule}    from '@angular/http';
import {FormsModule}   from '@angular/forms';
import {AppComponent}  from './app.component';
import {AuthService}   from "./auth/service/AuthService";

@NgModule({
  imports: [BrowserModule, HttpModule, FormsModule],
  declarations: [AppComponent],
  bootstrap: [AppComponent],
  providers: [AuthService]
})
export class AppModule {
}
