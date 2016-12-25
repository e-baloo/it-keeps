/**
 * Created by Marc on 13/12/2016.
 */

import {Injectable } from '@angular/core';
import {Response, Headers, Http, RequestOptionsArgs, RequestOptions} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/Rx';
import {AuthHttp} from "angular2-jwt";

@Injectable()
export class PathService {
  constructor(private authHttp: AuthHttp) {
  }

  private itkeepsUrl: string = 'http://localhost:8001/api/path'

  getAll(): Observable<string[]> {
    return this.authHttp.get(this.itkeepsUrl + '/get-all/').map((res: Response) => <string[]> res.json());
  }


}
