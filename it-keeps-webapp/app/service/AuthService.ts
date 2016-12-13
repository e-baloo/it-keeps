/**
 * Created by Marc on 13/12/2016.
 */

import {Injectable} from '@angular/core';
import {Http, Response,} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/Rx';

@Injectable()
export class AuthService {
  constructor(private http: Http) {
  }

  getEnumAuthType(): Observable<any> {
    return this.http.get('http://localhost:8001/api/enum/auth').map((res: Response) => res.json());
  }

}
