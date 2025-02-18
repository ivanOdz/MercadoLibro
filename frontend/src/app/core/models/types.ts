import {Exchange} from "./exchange.model";
import {Message} from "./message.model";
import {Location} from "./location.model";
import {User} from "./user.model";
import {BookModel} from "./bookModel.model";
import {Observable} from "rxjs";
import {Pagination} from "./pagination";
import {Publication} from "./publication.model";

export type ObservablePublicationData =
    Observable<{
        publicationData: PublicationData[],
        pagination: Pagination,
        headers: {
            conditionHeaders: Record<string, string>,
            genreHeaders: Record<string, string>
        },
		totalResults: number
    }>



export type ObservablePublication =
    Observable<{
        publications: Publication[],
        pagination: Pagination,
        headers: {
            conditionHeaders: Record<string, string>,
            genreHeaders: Record<string, string>
		},
		totalResults: number
    }>



export type ExchangeData = {
    exchange: Exchange,
    offeredPub: PublicationData,
    requestedPub: PublicationData,
    messages: Message[]
};

export type FavoritePublication = {
    publication: string | null
    user: string | null;
    likedAt: Date | null;
    self: string | null;
}

export type PublicationData = {
    book: BookData | null;
    locations: Location[] | null;
    user: User | null;
    publicationState: string | null;
    publicationDatetime: Date | null;
    favoriteEndpoint: string | null; // The endpoint in which a POST should be done in order to mark a publication as favorite.
    favoritePublication: FavoritePublication | null // If it is null, this publications it is not favorite for logged user. Manually query this field
    isFavoriteTemplate: string;
    self: string | null,
    publication: Publication | null
}

export type BookData = {
    state: string;
    available: boolean | null;
    owner: User | null;
    bookModel: BookModel | null;
    images: string[] | null;
    self: string | null;
}
