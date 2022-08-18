import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ParedeFormService, ParedeFormGroup } from './parede-form.service';
import { IParede } from '../parede.model';
import { ParedeService } from '../service/parede.service';
import { ISala } from 'app/entities/sala/sala.model';
import { SalaService } from 'app/entities/sala/service/sala.service';

@Component({
  selector: 'jhi-parede-update',
  templateUrl: './parede-update.component.html',
})
export class ParedeUpdateComponent implements OnInit {
  isSaving = false;
  parede: IParede | null = null;

  salasSharedCollection: ISala[] = [];

  editForm: ParedeFormGroup = this.paredeFormService.createParedeFormGroup();

  constructor(
    protected paredeService: ParedeService,
    protected paredeFormService: ParedeFormService,
    protected salaService: SalaService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareSala = (o1: ISala | null, o2: ISala | null): boolean => this.salaService.compareSala(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ parede }) => {
      this.parede = parede;
      if (parede) {
        this.updateForm(parede);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const parede = this.paredeFormService.getParede(this.editForm);
    if (parede.id !== null) {
      this.subscribeToSaveResponse(this.paredeService.update(parede));
    } else {
      this.subscribeToSaveResponse(this.paredeService.create(parede));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParede>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(parede: IParede): void {
    this.parede = parede;
    this.paredeFormService.resetForm(this.editForm, parede);

    this.salasSharedCollection = this.salaService.addSalaToCollectionIfMissing<ISala>(this.salasSharedCollection, parede.sala);
  }

  protected loadRelationshipsOptions(): void {
    this.salaService
      .query()
      .pipe(map((res: HttpResponse<ISala[]>) => res.body ?? []))
      .pipe(map((salas: ISala[]) => this.salaService.addSalaToCollectionIfMissing<ISala>(salas, this.parede?.sala)))
      .subscribe((salas: ISala[]) => (this.salasSharedCollection = salas));
  }
}
