import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IParede } from '../parede.model';

@Component({
  selector: 'jhi-parede-detail',
  templateUrl: './parede-detail.component.html',
})
export class ParedeDetailComponent implements OnInit {
  parede: IParede | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ parede }) => {
      this.parede = parede;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
