import {Base} from "./Base";
/**
 * Created by donval on 14/12/2016.
 */


export class jCredential extends Base {

  private password64: string;
  private userName: string;


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

}
