import { Component } from '@angular/core';
import {NavigationEnd, Router, RouterModule} from '@angular/router';
import {Title} from "@angular/platform-browser";
import {TranslateService} from "@ngx-translate/core";
import {filter} from "rxjs";
import {LanguageService} from "./core/services/language.service";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterModule],
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.css']
})
export class AppComponent {
  title = 'Mercado Libro';


  constructor(private router: Router,
              private titleService: Title,
              private translate: TranslateService,
              private languageService: LanguageService) {
    this.router.events
        .pipe(filter(event => event instanceof NavigationEnd))
    .subscribe(() => {
      this.setTitle();
    })
  }


  setTitle() {
    let route = this.router.routerState.snapshot.root;

    while (route.firstChild) {
      route = route.firstChild;
    }

    const titleKey = route.data?.['title'];

    if (titleKey) {
        this.titleService.setTitle(this.translate.instant(titleKey));
    }
  }

}
