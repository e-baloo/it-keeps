/**
 * Created by donval on 14/12/2016.
 */


export class jLight {

  private name: string;
  private id: string;
  private type: string;
  private ver: number;

  getName(): string {
    return this.name;
  }

  setName(value: string) {
    this.name = value;
  }

  getId(): string {
    return this.id;
  }

  getType(): string {
    return this.type;
  }

  getVersion(): number {
    return this.ver;
  }

}
