/**
 * Created by Marc on 13/12/2016.
 */

import {Injectable} from '@angular/core';
import {Http, Response, RequestOptions, Headers,} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/Rx';
import {jCredential} from "../../model/Credential";

@Injectable()
export class AuthService {
  constructor(private http: Http) {
  }

  private token: string = null;

  private itkeepsUrl: string = 'http://localhost:8001'

  getEnumAuthType(): Observable<string[]> {
    return this.http.get(this.itkeepsUrl + '/auth/type/enum').map((res: Response) => <string[]> res.json());
  }

  login(cred: jCredential): Observable<boolean> {

    let headers = new Headers({ 'Content-Type': 'text/plain' });
    let options = new RequestOptions({ headers: headers });

    return this.http.post(this.itkeepsUrl + '/auth/login/', cred, options)
      .map((response: Response) => {
        let token = response.json() && response.json().token;
        if (token) {
          this.token = token;
          localStorage.setItem('token', token);
          return true;
        } else {
           // return false to indicate failed login
          return false;
        }

      });
  }


  logout(): void {
    // clear token remove user from local storage to log user out
    this.token = null;
    localStorage.removeItem('token');
  }

}
