/**
 * Created by Marc on 13/12/2016.
 */

import {Injectable, EventEmitter} from '@angular/core';
import {Http, Response, RequestOptions, Headers,} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/Rx';
import {jCredential} from "../model/jCredential";
import {JwtHelper} from "angular2-jwt";

@Injectable()
export class AuthService {

	private static _instance:AuthService = null;
  
  private loggedInEmitter = new EventEmitter<boolean>();
  private roleEmitter = new EventEmitter<string>();
  
  constructor(private http: Http) {
		if(!AuthService._instance){
			AuthService._instance = this;
		}
  
    this.loggedInEmitter.emit(false);
	}

	public static getInstance():AuthService {
		return AuthService._instance;
	}
  
	
	
  
  private privateGetToken(): string {
    return localStorage.getItem(AuthService.TOKEN_NAME);
  }
  
  private privateSetToken(token: string): void {
    localStorage.setItem(AuthService.TOKEN_NAME, token);
    this.loggedInEmitter.emit(true);
    this.roleEmitter.emit(this.getUserRole());
  }
  
  private privateRemoveToken(): void {
    localStorage.removeItem(AuthService.TOKEN_NAME);
    this.loggedInEmitter.emit(false);
    this.roleEmitter.emit(null);
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
    if(this.privateGetToken()) {
  
      let headers = new Headers({'Content-Type': 'application/json'});
      headers.append('x-access-token', this.privateGetToken());
      let options = new RequestOptions({headers: headers});
  
      this.http.put(this.getBackendUrl() + 'renew/', null, options)
        .map((response: Response) => response.json())
        .subscribe(
          json => this.setToken(json),
          function (error) {
            this.privateRemoveToken();
            console.error("renew : " + error);
          }
        );
    }
  }
  
  public logout(): void {
	  if(this.privateGetToken()) {
	    
      let headers = new Headers({'Content-Type': 'application/json'});
      headers.append('x-access-token', this.privateGetToken());
      let options = new RequestOptions({headers: headers});
    
      this.privateRemoveToken();
    
      this.http.delete(this.getBackendUrl() + 'logout/', options);
    }
    
  }

	private setToken(json: any): boolean {
    if(!json) {
      this.privateRemoveToken();
      return false;
    }

		let token = json.token;

    if(!token) {
      this.privateRemoveToken();
      return false;
    }
    
    this.privateRemoveToken();
    this.privateSetToken(token);
    return true;

	}

	public isTokenExpired(): boolean {

		let token = this.getToken();

		if(!token) {
			return false;
		}

		return AuthService.jwtHelper.isTokenExpired(token);

	}



	public getToken(): string {

		let token  = this.privateGetToken();

		if(!token) {
			return null;
		}

		if(AuthService.jwtHelper.isTokenExpired(token)) {
		  this.privateRemoveToken();
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





	public log() {

		let token = this.privateGetToken();

		console.log(
			token,
			AuthService.jwtHelper.decodeToken(token),
			AuthService.jwtHelper.getTokenExpirationDate(token),
			AuthService.jwtHelper.isTokenExpired(token)
		);

	}

	
	public isTokenIsValid(): boolean {
    
    let token = this.privateGetToken();
    
    if(!token) {
      console.log("token is empty !!!");
      return false;
    }
	  
    return !AuthService.jwtHelper.isTokenExpired(token);
	  
  }
	
	
	public getUserId(): string {
    let token = this.privateGetToken();

    if(!token) {
      return null;
    }

    return AuthService.jwtHelper.decodeToken(token).id;
  }

  public getUserName(): string {
    let token = this.privateGetToken();

    if(!token) {
      return null;
    }

    return AuthService.jwtHelper.decodeToken(token).name;
  }

  public getUserRole(): string {
    let token = this.privateGetToken();

    if(!token) {
      return null;
    }

    return AuthService.jwtHelper.decodeToken(token).role;
  }

  //http://stackoverflow.com/questions/37024918/angular2-changedetection-or-observable
  //http://stackoverflow.com/questions/38284173/how-to-get-angularfire2-authentication-as-an-observable
  //https://blog.sstorie.com/building-an-angular-2-reactive-auto-logout-timer-with-the-redux-pattern/
  
  
  //Observable<TestModel>((subscriber: Subscriber<TestModel>) => subscriber.next(new TestModel())).map(o => JSON.stringify(o)
  
  getLoggedInEmitter() : EventEmitter<boolean> {
	  return this.loggedInEmitter;
  }
  
  getRoleEmitter() : EventEmitter<string> {
	  return this.roleEmitter;
  }
  
  
}
