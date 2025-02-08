export class Message {
    message: string;
    time: Date;
    self: string;
    user: string;
    exchange: string;

    constructor(data: any) {
        this.message = data.message;
        this.time = data.time;
        this.self = data.self;
        this.user = data.user;
        this.exchange = data.exchange;

    }
}
