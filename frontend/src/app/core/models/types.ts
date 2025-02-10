import {Exchange} from "./exchange.model";
import {Message} from "./message.model";
import {Location} from "./location.model";
import {User} from "./user.model";
import {BookModel} from "./bookModel.model";


export type ExchangeData = {
    exchange: Exchange,
    offeredPub: PublicationData,
    requestedPub: PublicationData,
    messages: Message[]
};

export type PublicationData = {
    book: BookData,
    locations: Location[]
};

export type BookData = {
    owner: User | null,
    model: BookModel | null
};

export type FavoritePublication = {
    publication: string | null
    user: string | null;
    likedAt: Date | null;
    self: string | null;
}

export type PublicationData2 = {
    book: BookData2 | null;
    locations: Location[] | null;
    user: User | null;
    publicationState: string | null;
    publicationDatetime: Date | null;
    favoriteEndpoint: string | null; // The endpoint in which a POST should be done in order to mark a publication as favorite.
    favoritePublication: FavoritePublication | null // If it is null, this publications it is not favorite for logged user. Manually query this field
    isFavoriteTemplate: string;
    self: string | null,
}

export type BookData2 = {
    state: string | null;
    available: boolean | null;
    owner: string | null;
    bookModel: BookModel | null;
    images: string[] | null;
    self: string | null;
}
