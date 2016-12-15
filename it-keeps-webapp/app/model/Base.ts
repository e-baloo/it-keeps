import {jLight} from "./Light";
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

}
