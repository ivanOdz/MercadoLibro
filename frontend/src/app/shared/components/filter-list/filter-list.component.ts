import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { NgForOf, UpperCasePipe } from "@angular/common";
import { Card } from "primeng/card";
import { PrimeTemplate } from "primeng/api";
import { Router } from '@angular/router';

@Component({
    selector: 'app-filter-list',
    templateUrl: './filter-list.component.html',
    standalone: true,
    imports: [
        UpperCasePipe,
        TranslatePipe,
        Card,
        PrimeTemplate,
        NgForOf
    ],
    styleUrls: ['./filter-list.component.css']
})
export class FilterListComponent implements OnInit {
    @Input() headers: Record<string, string> = {};
    @Input() filterType: 'Condition' | 'Genre' = 'Condition';

    filters: { labelKey: string; count: number; queryParam: string }[] = [];

    constructor(private router: Router) {}

    ngOnInit() {
        this.parseHeaders();
    }

    parseHeaders() {
        const prefix = this.filterType === 'Condition' ? 'bookstate.' : 'genre.';
        const paramName = this.filterType === 'Condition' ? 'state' : 'genre';

        this.filters = Object.entries(this.headers)
            .map(([key, value]) => {
                const match = value.match(new RegExp(`${prefix}(.*?)=(\\d+)`));
                if (!match) return null;

                const valueKey = match[1];
                const formattedLabelKey = `${prefix}${valueKey}`;
                const count = parseInt(match[2], 10);
                const queryParam = `${paramName}=${formattedLabelKey}`;

                return { labelKey: formattedLabelKey, count, queryParam };
            })
            .filter(filter => filter !== null) as { labelKey: string; count: number; queryParam: string }[];
    }

    onFilterClick(filter: { queryParam: string }) {
        const queryParam = filter.queryParam;

        const param = queryParam.split('=')[0];
        const value = queryParam.split('=')[1];

        this.router.navigate([], {
            queryParams: { [param]: value },
            queryParamsHandling: 'merge'
        });
    }
}
