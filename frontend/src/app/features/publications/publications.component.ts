import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";
import {FilterListComponent} from "../../shared/components/filter-list/filter-list.component";
import {PublicationService} from "../../core/services/publication.service";

@Component({
  selector: 'app-publications',
  imports: [RouterModule, NavbarComponent, FilterListComponent],
  templateUrl: `./publications.component.html`,
  standalone: true,
  styleUrl: './publications.component.css'
})
export class PublicationsComponent {

  conditionHeaders = {
    "X-bookstate-new": "bookstate.new=5",
    "X-bookstate-like-new": "bookstate.like.new=2",
    "X-bookstate-very-good": "bookstate.very.good=8",
    "X-bookstate-good": "bookstate.good=1",
    "X-bookstate-acceptable": "bookstate.acceptable=6",
    "X-bookstate-worn": "bookstate.worn=4"
  };

  genreHeaders = {
    "X-genre-fiction": "genre.fiction=12",
    "X-genre-non-fiction": "genre.non.fiction=8",
    "X-genre-mystery": "genre.mystery=4"
  };

  constructor(private publicationService: PublicationService) {

  }

  onFilterSelected(url: string) {
    this.publicationService.getPublications(url).subscribe({
      next: (response) => console.log("Response:", response),
      error: (err) => console.error("Error:", err)
    });
  }


}
