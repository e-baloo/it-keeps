/**
 * Created by Marc on 13/12/2016.
 */

import {Injectable } from '@angular/core';
import {Response, Headers, Http, RequestOptionsArgs, RequestOptions} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/Rx';
import {AuthHttp} from "angular2-jwt";

@Injectable()
export class AclService {
  constructor(private authHttp: AuthHttp) {
  }

  private itkeepsUrl: string = 'http://localhost:8001/api/acl'

  getEnumRole(): Observable<string[]> {
    return this.authHttp.get(this.itkeepsUrl + '/role/enum/').map((res: Response) => <string[]> res.json());
  }

  getEnumAdmin(): Observable<string[]> {
    return this.authHttp.get(this.itkeepsUrl + '/admin/enum/').map((res: Response) => <string[]> res.json());
  }

  getEnumOwner(): Observable<string[]> {
    return this.authHttp.get(this.itkeepsUrl + '/owner/enum/').map((res: Response) => <string[]> res.json());
  }

  getEnumData(): Observable<string[]> {
    return this.authHttp.get(this.itkeepsUrl + '/data/enum/').map((res: Response) => <string[]> res.json());
  }

}
