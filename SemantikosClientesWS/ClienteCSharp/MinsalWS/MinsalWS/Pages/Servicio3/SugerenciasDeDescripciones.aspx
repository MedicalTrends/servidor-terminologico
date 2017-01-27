<%@ Page Title="" Language="C#" MasterPageFile="~/Servicios.Master" AutoEventWireup="true" CodeBehind="SugerenciasDeDescripciones.aspx.cs" Inherits="MinsalWS.Pages.Servicio3.SugerenciasDeDescripciones" %>
<asp:Content ID="Content1" ContentPlaceHolderID="HeadContent" runat="server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">
    <div style="width:100%;">
    <table style="width:100%;">
        <tr>
            <td>
                <asp:Label ID="lblTermino" Text="Término:" runat="server"></asp:Label>
            </td>
            <td>
                <asp:TextBox ID="txtTermino" Text="" runat="server"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td>
                <asp:Label ID="lblNombreCat" Text="Nombre categoría:" runat="server"></asp:Label>
            </td>
            <td>
                <asp:TextBox ID="txtNombreCat" Text="" runat="server"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td>
                <asp:Label ID="lblNombreRefSet" Text="Nombre ref set:" runat="server"></asp:Label>
            </td>
            <td>
                <asp:TextBox ID="txtNombreRefSet" Text="" runat="server"></asp:TextBox>
            </td>
        </tr>
        <tr>
            <td>
                
            </td>
            <td>
                <asp:Button ID="btnInvocar" Text="Invocar" runat="server" OnClick="btnInvocar_Click"></asp:Button>
            </td>
        </tr>
        </table>
        <asp:TextBox id="TextArea1" TextMode="multiline" Columns="50" Rows="5" runat="server" Width="100%"/>
    </div>

</asp:Content>