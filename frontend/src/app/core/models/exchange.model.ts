export class Exchange {
    state: string;
    accept_code: number;
    offerer_received: boolean;
    requester_received: boolean;
    isConfirmed: boolean;
    start_date: Date;
    end_date: Date;
    self: string;
    offerer: string;
    requester: string;
    chat: string;
    offererReview: string;
    requesterReview: string;

    constructor(data: any) {
        this.state = data.state;
        this.accept_code = data.accept_code;
        this.offerer_received = data.offerer_received;
        this.requester_received = data.requester_received;
        this.isConfirmed = data.isConfirmed;
        this.start_date = data.start_date;
        this.end_date = data.end_date;
        this.self = data.self;
        this.offerer = data.offerer;
        this.requester = data.requester;
        this.chat = data.chat;
        this.offererReview = data.offererReview;
        this.requesterReview = data.requesterReview;
    }

    getIsConfirmed() {
        return this.isConfirmed;
    }
}
