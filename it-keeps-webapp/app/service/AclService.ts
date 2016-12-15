/**
 * Created by Marc on 13/12/2016.
 */

import {Injectable } from '@angular/core';
import {Response, Headers, Http, RequestOptionsArgs, RequestOptions} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/Rx';

@Injectable()
export class AclService {
  constructor(private http: Http) {
  }

  private itkeepsUrl: string = 'http://localhost:8001/api/acl'

  getEnumAclRole(): Observable<string[]> {
    let token = localStorage.getItem('token');

/*

    let headers = new Headers();
    headers.append('Content-Type', 'text/plain');
    headers.append('Authorization', 'Bearer ' + token);
    //headers.append('x-access-token', token);

    RequestOptions options = new RequestOptions();
    options.
      */
//    let headers = new Headers({ 'Content-Type': 'text/plain' });
//    let headers = new Headers({ 'Authorization': 'Bearer ' + token });
//    headers.append('Content-Type', 'text/plain');
//    let options = new RequestOptions({ headers: headers, withCredentials: true });

    //options.headers.

    /*
    let headers: Headers = new Headers();
    headers.append("content-type", 'application/json');
    headers.append("authorization", 'Bearer xxxxxxxxxxxxxxxxxxxxxxxxx')

    let requestoptions: RequestOptions = new RequestOptions({
      headers: headers,
      withCredentials: true
    });

    console.log(requestoptions);
  */

    let headers = new Headers();

    headers.append('content-type', 'plain/text');
    //headers.append('authorization', ('bearer ' + token));
    headers.append('x-access-token', token);

    return this.http.get(this.itkeepsUrl + '/role/enum/', {headers: headers}).map((res: Response) => <string[]> res.json());
  }


}
