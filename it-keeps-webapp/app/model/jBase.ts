import {jLight} from "./jLight";
/**
 * Created by donval on 14/12/2016.
 */

export class Base {

  light: jLight

  getLight() {
    if(!this.light) {
      this.light = new jLight;
    }

    return this.light;
  }

  getName(): string {
    return this.getLight().getName();
  }

  setName(value: string) {
    this.getLight().setName(value);
  }

  getId(): string {
    return this.getLight().getId();
  }
}
