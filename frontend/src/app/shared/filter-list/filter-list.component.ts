import {Component, Input, OnInit, SimpleChanges, OnChanges, EventEmitter, Output } from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';
import { NgForOf, UpperCasePipe } from "@angular/common";
import { Card } from "primeng/card";
import { PrimeTemplate } from "primeng/api";
import { Router } from '@angular/router';
import { ChangeDetectionStrategy } from '@angular/core';

@Component({
    selector: 'app-filter-list',
    templateUrl: './filter-list.component.html',
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [
        UpperCasePipe,
        TranslatePipe,
        Card,
        PrimeTemplate,
        NgForOf
    ],
    styleUrls: ['./filter-list.component.css']
})
export class FilterListComponent implements OnInit, OnChanges  {
    @Input() headers: Record<string, string> = {};
	@Input() filterApplied: boolean = false;
    @Input() filterType: 'Condition' | 'Genre' = 'Condition';
	@Output() filterChanged = new EventEmitter<{ param: string, value: string }>();
	
    filters: { labelKey: string; count: number; queryParam: string }[] = [];

    constructor(private router: Router) {}

    ngOnInit() {
        this.parseHeaders();
    }

    ngOnChanges(changes: SimpleChanges) {
        if (changes["headers"]?.currentValue && !this.filterApplied) {
            this.parseHeaders();
        }
    }

    parseHeaders() {
        const prefix = this.filterType === 'Condition' ? 'bookstate.' : 'genre.';
        const paramName = this.filterType === 'Condition' ? 'state' : 'genre';
        this.filters = Object.keys(this.headers)
            .map((key) => {
                const value = this.headers[key]; // Accede usando corchetes
                if (!value) return null;

                const match = value.match(new RegExp(`${prefix}(.*?)=(\\d+)`));
                if (!match) return null;

                const valueKey = match[1];
                const formattedLabelKey = `${prefix}${valueKey}`;
                const count = parseInt(match[2], 10);
                const queryParam = `${paramName}=${formattedLabelKey}`;

                return { labelKey: formattedLabelKey, count, queryParam };
            })
            .filter((filter): filter is { labelKey: string; count: number; queryParam: string } => filter !== null);
    }

    onFilterClick(filter: { queryParam: string }) {
        const queryParam = filter.queryParam;
        const param = queryParam.split('=')[0];
        const value = queryParam.split('=')[1];
		
        this.router.navigate([], {
            queryParams: { [param]: value },
            queryParamsHandling: 'merge'
        });
		
		this.filterChanged.emit({ param, value });
		this.filters = this.filters.filter(f => f.queryParam === queryParam);
    }
}
