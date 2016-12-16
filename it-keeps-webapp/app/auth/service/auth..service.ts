/**
 * Created by Marc on 13/12/2016.
 */

import {Injectable} from '@angular/core';
import {Http, Response, RequestOptions, Headers,} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/Rx';
import {jCredential} from "../../model/jCredential";
import {JwtHelper, AuthHttp} from "angular2-jwt";

@Injectable()
export class AuthService {

	private static _instance:AuthService = null;


	constructor(private http: Http) {
		if(!AuthService._instance){
			AuthService._instance = this;
		}
	}

	public static getInstance():AuthService
	{
		return AuthService._instance;
	}


	public static readonly TOKEN_NAME: string = 'token';
	private static readonly jwtHelper: JwtHelper = new JwtHelper();



	private getBackendUrl(): string {

		return 'http://localhost:8001/' + 'auth/'

	}

	public getAuthTypeEnum(): Observable<string[]> {

		return this.http.get(this.getBackendUrl() + 'type/enum').map((res: Response) => <string[]> res.json());

	}

	public login(cred: jCredential): Observable<boolean> {

		let headers = new Headers({ 'Content-Type': 'application/json' });
		let options = new RequestOptions({ headers: headers });

		return this.http.post(this.getBackendUrl() + 'login/', cred, options)
			.map((response: Response) => this.setToken(response.json()));
	}

	public renew(): void {
    let headers = new Headers({'Content-Type': 'application/json'});
    headers.append('x-access-token', this._getToken());
    let options = new RequestOptions({headers: headers});
    this.http.get(this.getBackendUrl() + 'renew/', options)
      .map((response: Response) => response.json())
      .subscribe(
        json => this.setToken(json),
        error => console.error(error)
      );
  }


	private setToken(json: any): boolean {
    if(!json) {
      localStorage.removeItem(AuthService.TOKEN_NAME);
      return false;
    }

		let token = json.token;

    if(!token) {
      localStorage.removeItem(AuthService.TOKEN_NAME);
      return false;
    }

    localStorage.removeItem(AuthService.TOKEN_NAME);
    localStorage.setItem(AuthService.TOKEN_NAME, token);
    return true;

	}

	public isTokenExpired(): boolean {

		let token = this.getToken();

		if(!token) {
			return false;
		}

		return AuthService.jwtHelper.isTokenExpired(token);

	}


	private _getToken(): string {
		return localStorage.getItem(AuthService.TOKEN_NAME);
	}


	public getToken(): string {

		let token  = this._getToken();

		if(!token) {
			return null;
		}

		if(AuthService.jwtHelper.isTokenExpired(token)) {
			localStorage.removeItem(AuthService.TOKEN_NAME);
			return null;
		}

		let decodeToken: any = AuthService.jwtHelper.decodeToken(token);
		let exp: number = decodeToken.exp;
		let iat: number = decodeToken.iat;
		let cur: number = (new Date()).valueOf() / 1000;
		let rnw: number = iat + ((exp - iat) /2);

		if(cur > rnw) {
			this.renew();
		}

		return token;
	}


	public logout(): void {
		localStorage.removeItem(AuthService.TOKEN_NAME);
	}


	public log() {

		let token = this._getToken();

		console.log(
			token,
			AuthService.jwtHelper.decodeToken(token),
			AuthService.jwtHelper.getTokenExpirationDate(token),
			AuthService.jwtHelper.isTokenExpired(token)
		);

	}

	public getUserIt(): string {
    let token = this._getToken();

    if(!token) {
      return null;
    }

    return AuthService.jwtHelper.decodeToken(token).id;
  }

  public getUserName(): string {
    let token = this._getToken();

    if(!token) {
      return null;
    }

    return AuthService.jwtHelper.decodeToken(token).name;
  }

  public getUserRole(): string {
    let token = this._getToken();

    if(!token) {
      return null;
    }

    return AuthService.jwtHelper.decodeToken(token).role;
  }

}
