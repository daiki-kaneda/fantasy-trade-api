## FantasyTradeAPI
ユーザー間でのアイテムトレードを安全に行うためのバックエンドAPIです。 このプロジェクトは、**「データベースの並行性制御と整合性の担保」**を実地で学ぶことを主目的として開発しました。

🚀 技術スタック
Backend: Java 21 / Spring Boot 3.5

Security: Spring Security / Firebase Authentication

Database: SQL

Design: Domain-Driven Design (DDD) principles

🛠 注力した技術ポイント
1. 悲観ロックによる整合性の確保
トレード処理において、複数のリクエストが同時に発生してもデータの不整合（二重引き落とし等）が起きないよう、SELECT FOR UPDATE による悲観ロックを実装しています。

1. デッドロックの防止戦略
複数のリソース（ユーザーAとユーザーBの資産）をロックする際、デッドロックが発生するリスクがあります。本プロジェクトでは、**「常にIDの昇順でロックを取得する」**というルールをコードレベルで強制し、循環待ちを回避しています。

1. Firebase Auth 連携
Spring Securityのフィルタチェーンをカスタマイズし、Firebaseが発行したIDトークンを用いた認証基盤を構築しています。

🏗 トレード処理のフロー
コード スニペット

``` mermaid
sequenceDiagram
    participant App as Client
    participant API as Spring Boot
    participant DB as DB

    App->>API: トレード実行リクエスト (ID Token付)
    API->>API: Firebase Token検証
    API->>DB: 参加ユーザーをID順でロック (FOR UPDATE)
    Note over DB: デッドロック防止のためID昇順で取得
    DB-->>API: ロック取得成功
    API->>API: ビジネスルール検証 (残高確認等)
    API->>DB: 資産移動の更新
    API->>DB: トランザクション・コミット
    DB-->>API: 完了
    API-->>App: 200 OK
```