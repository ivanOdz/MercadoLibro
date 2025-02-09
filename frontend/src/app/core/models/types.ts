import {Exchange} from "./exchange.model";
import {Message} from "./message.model";
import {Location} from "./location.model";
import {User} from "./user.model";
import {BookModel} from "./bookModel.model";


export type ExchangeData = {exchange: Exchange, offeredPub: PublicationData, requestedPub: PublicationData, messages: Message[]};
export type PublicationData = {book: BookData, locations: Location[]};
export type BookData = {owner: User | null, image: string | null, model: BookModel | null};
export type message = { sender: number, message: string, date: Date };
