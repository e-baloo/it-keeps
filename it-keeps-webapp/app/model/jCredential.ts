import {Base} from "./jBase";
/**
 * Created by donval on 14/12/2016.
 */


export class jCredential extends Base {

  private password64: string;
  private userName: string;
  private authenticationType: string;


  getPassword64(): string {
    return this.password64;
  }

  setPassword64(value: string) {
    this.password64 = value;
  }

  getPassword(): string {
    return atob(this.getPassword64());
  }
  setPassword(value: string) {
    this.setPassword64(btoa(value));
  }

  getUserName(): string {
    return this.userName;
  }

  setUserName(value: string) {
    this.userName = value;
  }

  setAuthenticationType(authenticationType: string) {
    this.authenticationType = authenticationType;
  }

  getAuthenticationType(): string {
    return this.authenticationType;
  }



}
